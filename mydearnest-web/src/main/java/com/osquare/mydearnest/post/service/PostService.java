package com.osquare.mydearnest.post.service;

import java.util.Collection;
import java.util.List;

import com.osquare.mydearnest.entity.Account;
import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.entity.ImageSource;
import com.osquare.mydearnest.entity.Post;
import com.osquare.mydearnest.entity.PostComment;
import com.osquare.mydearnest.entity.PostGrade;
import com.osquare.mydearnest.entity.PostLove;
import com.osquare.mydearnest.post.vo.PostVO;
import com.osquare.mydearnest.profile.vo.CommentVO;


public interface PostService {
	
	Collection<Folder> getUserFolders(Account account);
	Collection<Folder> getUserFolders(Account account, long folder_id);
	Folder getFolder(Long folderId);
	Folder createFolder(String folderName, String desc, Account account);

	Collection<Post> getPostsOrderByLatest(Integer page, Long category);
	Collection<Post> getPostsOrderByRank(Integer page);
	
	Collection<Post> getPostsByFolder(Folder folder, Integer page);
	Collection<Post> getPostsByKeyword(String keyword, Integer page);
	
	Collection<Post> getPostsByAccount(Account account);
	Collection<Post> getPostsByAccount(Account account, Integer page);

	Collection<Post> getPostsByLove(Account account, Integer page);
	
	Post getPostById(Long postId);
	
	Post createPostUpload(Account account, ImageSource imageSource, PostVO postVO);
	Post createPostDetail(Account account, ImageSource imageSource, PostVO postVO);
	PostGrade createPostGrade(Post post, Account account, PostVO postVO);
	
	Post removePostByMode(Long postId, String editMode, Long drawerId);
	
	Collection<PostComment> getCommentsByPost(Post post, Integer page);
	PostComment createPostComment(Post post, Account account, String text);
	
	PostLove createPostLove(Account account, Long postId);
	PostLove findPostLoveByAccount(Account account, Post post);
	void deletePostLove(PostLove postLove);
	
	
	
	
	Long getPrevPostId(Long postId, String ref);
	Long getNextPostId(Long postId, String ref);
	List<PostLove> getPostLoveUsers(Post post);

	List<Account> getPostRefindUsers(Long postId, int size);
	long getPostRefindCount(Long postId);

	void createPostView(Account account, Post post);
	Collection<Post> getReadedPostsByFolder(long account_id, Integer page);
	void removeFolder(Long folder_id, Long accountId) throws Exception;
	
	List<CommentVO> findPostCommentByAccountId(Long accountId, Integer page);
	boolean deletePost(Long postId);
	
}
