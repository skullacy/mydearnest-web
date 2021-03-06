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
import com.osquare.mydearnest.entity.PostUserGrade;
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
	Post getPostByRandom(Integer checksum);
	Post getPostByRandom(Integer checksum, String type, Account account);
	
	Post createPostUpload(Account account, ImageSource imageSource, PostVO postVO);
	Post createPostDetail(Post post, Account account, PostVO postVO);
	Post createPostPhotoTag(Post post, Account account, PostVO postVO);
	PostGrade createPostGrade(Post post, Account account, PostVO postVO);
	PostGrade updatePostGrade(Post post, Account account, PostVO postVO);
	Post checkPostPublishable(Post post);
	
	PostUserGrade createPostUserGrade(Post post1, Account account, PostUserGrade postUserGrade);
	PostUserGrade updatePostUserGrade(Post post1, Account account, PostUserGrade postUserGrade);
	
	Post removePostByMode(Long postId, String editMode, Long drawerId);
	
	Collection<PostGrade> getPostGradeByPost(Post post);
	PostGrade getMyPostGradeByPost(Account account, Post post);
	
	Collection<PostUserGrade> getPostUserGradeByPost(Post post);
	
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
