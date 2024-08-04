import prisma from "../db";
import { hashPassword, createJWT, comparePassword } from '../modules/auth';

export const createNewUser = async (req, res, next) => {
  try {
    const user = await prisma.user.create({
      data: {
        username: req.body.username,
        password: await hashPassword(req.body.password)
      }
    })
    const token = createJWT(user);
    res.json({ token });
  } catch (err) {
    err.type = 'input';
    next(err);
  }
}

export const signin = async (req, res) => {
  const user = await prisma.user.findUnique({
    where: {
      username: req.body.username
    }
  })

  const isValid = await comparePassword(req.body.password, user.password);

  if (!isValid) {
    return res.status(401).json({ message: 'Invalid credentials' });
  }

  const token = createJWT(user);
  res.json({ token });
}