package com.web.blog.controller.post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.post.LikeListDao;
import com.web.blog.dao.post.PostListDao;
import com.web.blog.model.post.LikeList;
import com.web.blog.model.post.PostList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cart")
public class CartListController {

    @Autowired
    LikeListDao likeListDao;
    @Autowired
    PostListDao postListDao;

    @GetMapping("/list/{email}/{page}")
    @ApiOperation("장바구니 리스트")
    public List<PostList> selectAll(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        int start = (page - 1) * 8;
        int end = start + 8;
        
        List<LikeList> plist = new LinkedList<>();
        plist = likeListDao.findByEmailAndCart(email,1);

        List<PostList> list = new LinkedList<>();
        for (LikeList likeList : plist) {
            list.add(postListDao.findByPid(likeList.getPid()));
        }

        if(end > list.size()){
            end = list.size();
        }

        List<PostList> tlist = new LinkedList<>();
        for (int i = start; i < end; i++) {
            tlist.add(list.get(i));
        }

        return tlist;
    }

    @GetMapping("/count/{email}")
    @ApiOperation("장바구니 리스트 개수")
    public int countAll(@PathVariable String email) throws SQLException, IOException {
        List<LikeList> plist = new LinkedList<>();
        plist = likeListDao.findByEmailAndCart(email, 1);

        List<PostList> list = new LinkedList<>();
        for (LikeList likeList : plist) {
            list.add(postListDao.findByPid(likeList.getPid()));
        }
        return list.size();
    }

    @GetMapping("/regist/{email}/{pid}")
    @ApiOperation("장바구니 등록")
    public Object regist(@PathVariable String email, @PathVariable String pid) throws SQLException, IOException {
        try {
            LikeList list = new LikeList();
            list.setPid(Integer.parseInt(pid));
            list.setEmail(email);
            list.setCart(1);
            likeListDao.save(list);

            return list;
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/check/{email}/{pid}")
    @ApiOperation("장바구니 동일상품 확인")
    public Object check(@PathVariable String email, @PathVariable int pid) throws SQLException, IOException {
        LikeList like = likeListDao.findByEmailAndPidAndCart(email, pid, 1);
        if(like != null){ //이미 존재
            return true;
        }else{
            return false;
        }
    }
    
    
    @GetMapping("/likelist/{email}")
    @ApiOperation("like 리스트")
    public List<LikeList> selectLike(@PathVariable String email) throws SQLException, IOException {
        List<LikeList> list = new LinkedList<>();
        list = likeListDao.findByEmailAndCart(email, 1);
        return list;
    }
    
    @DeleteMapping("/delete/{no}")
    @ApiOperation("장바구니 삭제")
    public String delete(@PathVariable List<Integer> no) throws SQLException, IOException {
        likeListDao.deleteAll(likeListDao.findByNoIn(no));
        return "장바구니 삭제 완료";
    }
    
    @GetMapping("/preview/{no}")
    @ApiOperation("구매할 목록 미리보기")
    public List<PostList> preview(@PathVariable List<Integer> no) throws SQLException, IOException {
        
        List<LikeList> tlist = new LinkedList<>();
        tlist = likeListDao.findByNoIn(no);

        List<PostList> plist = new LinkedList<>();
        for (LikeList likeList : tlist) {
            plist.add(postListDao.findByPid(likeList.getPid()));
        }

        return plist;
    }
}