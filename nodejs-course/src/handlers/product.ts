import prisma from "../db"

// Get all 
export const getProducts = async (req, res) => {
  const user = await prisma.user.findUnique({
    where: {
      id: req.user.id,
    },
    include: {
      products: true,
    }
  });

  res.json({ data: user.products });
}

// Get one
export const getOneProduct = async (req, res) => {
  const product = await prisma.product.findFirst({
    where: {
      id: req.params.id,
      belongsToId: req.user.id,
    }
  });

  res.json({ data: product });
}

export const createProduct = async (req, res, next) => {
  try {
    const product = await prisma.product.create({
      data: {
        name: req.body.name,
        belongsToId: req.user.id,
      },
    });
    res.json({ data: product });
  } catch (err) {
    next(err);
  }
}

export const updateProduct = async (req, res) => {
  const update = await prisma.product.update({
    where: {
      id_belongsToId: {
        id: req.params.id,
        belongsToId: req.user.id,
      }
    },
    data: {
      name: req.body.name,
    },
  });

  res.json({ data: update });
}

export const deleteProduct = async (req, res) => {
  await prisma.product.delete({
    where: {
      id_belongsToId: {
        id: req.params.id,
        belongsToId: req.user.id,
      }
    },
  });

  res.json({ message: "Product deleted successfully" });
}
