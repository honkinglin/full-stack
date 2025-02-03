package com.fullstack.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fullstack.reader.entity.Book;
import com.fullstack.reader.service.BookService;
import com.fullstack.reader.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/management/book")
public class MBookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public ResponseUtils list(@RequestParam("page") Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        ResponseUtils resp = null;
        try {
            IPage p = bookService.selectBookMap(page, rows);
            resp = new ResponseUtils().put("list", p.getRecords()).put("count", p.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }

    @PostMapping("/upload")
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {
//        得到上传目录
        String uploadPath = request.getServletContext().getResource("/").getPath() + "/upload";
//        得到文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String format = sdf.format(new Date());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = format + suffix;
        file.transferTo(new File(uploadPath + "/" + filename));
        Map result = new LinkedHashMap();
        result.put("errno", 0);
        result.put("data", new String[]{"/upload/" + filename});
        return result;
    }

    @PostMapping("/create")
    public ResponseUtils createBook(@ModelAttribute Book book) {
        ResponseUtils resp;
        try {
            Document doc = Jsoup.parse(book.getDescription());
            Elements elements = doc.select("img");
            if (elements.size() == 0) {
                resp = new ResponseUtils("DescriptionError", "图书描述中必须包含一张图片");
                return resp;
            }
            Element img = elements.getFirst();
            String cover = img.attr("src");
            book.setCover(cover);
            book.setEvaluationScore(0f);
            book.setEvaluationQuantity(0);
            bookService.createBook(book);
            resp = new ResponseUtils().put("book", book);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }

        return resp;
    }

    @PostMapping("/update")
    public ResponseUtils updateBook(@ModelAttribute Book book) {
        ResponseUtils resp;
        try {
            Document doc = Jsoup.parse(book.getDescription());
            Elements elements = doc.select("img");
            if (elements.size() == 0) {
                resp = new ResponseUtils("DescriptionError", "图书描述中必须包含一张图片");
                return resp;
            }
            Element img = elements.getFirst();
            String cover = img.attr("src");
            Book b = bookService.selectById(book.getBookId());
            b.setBookName(book.getBookName());
            b.setSubTitle(book.getSubTitle());
            b.setAuthor(book.getAuthor());
            b.setCover(cover);
            b.setDescription(book.getDescription());
            b.setCategoryId(book.getCategoryId());
            bookService.updateBook(b);
            resp = new ResponseUtils().put("book", b);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }

    @PostMapping("/delete")
    public ResponseUtils deleteBook(@RequestParam("bookId") Long bookId) {
        ResponseUtils resp;
        try {
            bookService.deleteBook(bookId);
            resp = new ResponseUtils();
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }
}
