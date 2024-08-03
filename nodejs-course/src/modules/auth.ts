import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';

export const comparePassword = async (password, hash) => {
  return await bcrypt.compare(password, hash);
}

export const hashPassword = async (password) => {
  const salt = await bcrypt.genSalt(10);
  return await bcrypt.hash(password, salt);
}

export const createJWT = (user) => {
  const token = jwt.sign({ id: user.id, username: user.username }, process.env.JWT_SECRET);
  return token;
}

export const protect = (req, res, next) => {
  const bearer = req.headers.authorization;

  if (!bearer ||!bearer.startsWith('Bearer ')) {
    return res.status(401).json({ message: 'Access denied. No token provided.' });
  }

  const token = bearer.split(' ')[1];
  if (!token) {
    return res.status(401).json({ message: 'Access denied. No token provided.' });
  }

  try {
    const user = jwt.verify(token, process.env.JWT_SECRET);
    req.user = user;
    next();
  } catch (err) {
    console.error(err);
    return res.status(401).json({ message: 'Access denied. Invalid token.' });
  }
}