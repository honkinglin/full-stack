import { Router } from 'express';

const router = Router();

/**
 * Product
 */
router.get('/product', (req, res) => {
  res.json({success: true});
});
router.get('/product/:id', () => {});
router.put('/product/:id', () => {});
router.post('/product', () => {});
router.delete('/product/:id', () => {});

/**
 * Update
 */
router.get('/update', () => {});
router.get('/update/:id', () => {});
router.put('/update/:id', () => {});
router.post('/update', () => {});
router.delete('/update/:id', () => {});

/**
 * User
 */
router.get('/user', () => {});
router.get('/user/:id', () => {});
router.put('/user/:id', () => {});
router.post('/user', () => {});
router.delete('/user/:id', () => {});

export default router;