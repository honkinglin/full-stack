import express from 'express';
import morgan from 'morgan';
import cors from 'cors';

import router from './router';
import { protect } from './modules/auth';

const app = express();

app.get('/', (req, res, next) => {
  console.log('middle', req.url);
  next();
}, (req, res) => {
  console.log('hello world');
  res.status(200);
  res.json({ message: 'Hello, World!' });
});

app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(cors());

app.use('/api', protect, router);

export default app;
