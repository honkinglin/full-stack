package com.fullstack.mall.service.impl;

import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.model.dao.ProductMapper;
import com.fullstack.mall.model.pojo.Product;
import com.fullstack.mall.model.query.ProductListQuery;
import com.fullstack.mall.common.Constant.ProductListOrderBy;
import com.fullstack.mall.model.request.AddProductReq;
import com.fullstack.mall.model.request.ProductListReq;
import com.fullstack.mall.model.vo.CategoryVO;
import com.fullstack.mall.service.CategoryService;
import com.fullstack.mall.service.ProductService;
import com.fullstack.mall.utils.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProduct, product);
        Product productOld = productMapper.selectByName(updateProduct.getName());
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new MallException(MallExceptionEnum.NO_SUCH_PRODUCT);
        }
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword())
                    .append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService
                    .listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (Objects.nonNull(orderBy) && ProductListOrderBy.PRICE_ORDER_ENUM.contains(orderBy)) {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }

    @Override
    public void addProductByExcel(File destFile) throws IOException {
        List<Product> products = readProductsFromExcel(destFile);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Product productOld = productMapper.selectByName(product.getName());
            if (productOld != null) {
                throw new MallException(MallExceptionEnum.NAME_EXISTED);
            }
            int count = productMapper.insertSelective(product);
            if (count == 0) {
                throw new MallException(MallExceptionEnum.CREATE_FAILED);
            }
        }
    }

    private List<Product> readProductsFromExcel(File excelFile) throws IOException {
        ArrayList<Product> listProducts = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Product aProduct = new Product();

            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();

                switch (columnIndex) {
                    case 0:
                        aProduct.setName((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 1:
                        aProduct.setImage((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 2:
                        aProduct.setDetail((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 3:
                        Double cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setCategoryId(cellValue.intValue());
                        break;
                    case 4:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setPrice(cellValue.intValue());
                        break;
                    case 5:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setStock(cellValue.intValue());
                        break;
                    case 6:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setStatus(cellValue.intValue());
                        break;
                    default:
                        break;
                }
            }
            listProducts.add(aProduct);
        }
        workbook.close();
        inputStream.close();
        return listProducts;
    }
}
