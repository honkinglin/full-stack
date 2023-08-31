<?php

namespace app\controller\v1;

use app\exception\CategoryException;
use app\model\Category as CategoryModel;

class Category {
  public function getAllCategories() {
    $categories = CategoryModel::with('img')->select();
    if ($categories->isEmpty()) {
      throw new CategoryException();
    }
    return json($categories, 200);
  }
}