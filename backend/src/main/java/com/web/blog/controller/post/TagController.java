package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.LikeListDao;
import com.web.blog.dao.post.PostListDao;
import com.web.blog.dao.post.TagDao;
import com.web.blog.model.post.LikeList;
import com.web.blog.model.post.PostList;
import com.web.blog.model.post.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    TagDao tagDao;

    @GetMapping("/list/{pid}")
    @ApiOperation("태그 리스트")
    public Object regist(@PathVariable int pid) throws SQLException, IOException {
        List<String> tagname = new LinkedList<>();
        try {
            List<Tag> list = new LinkedList<>();
            list = tagDao.findByPid(pid);
            if (list != null){
                for(Tag tag : list){
                    tagname.add(tag.getTagname());
                }
                return new ResponseEntity<>(tagname, HttpStatus.ACCEPTED);
            }else
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}