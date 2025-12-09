package com.junghoon.studylog.service.post;

import com.junghoon.studylog.domain.post.Post;
import com.junghoon.studylog.domain.post.PostResponse;
import com.junghoon.studylog.domain.user.User;
import com.junghoon.studylog.dto.post.PostCreateRequest;
import com.junghoon.studylog.dto.post.PostUpdateRequest;
import com.junghoon.studylog.global.error.Exception400;
import com.junghoon.studylog.global.error.Exception403;
import com.junghoon.studylog.global.error.Exception404;
import com.junghoon.studylog.repository.post.PostRepository;
import com.junghoon.studylog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 글 작성
     */
    public PostResponse create(PostCreateRequest request, String email) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .tags(request.getTags())
                .isPrivate(request.isPrivate())
                .author(author)
                .build();

        Post saved = postRepository.save(post);
        return new PostResponse(saved);
    }

    /**
     * 내 글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getMyPosts(String email) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        return postRepository.findByAuthor(author)
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    /**
     * 게시글 단건 조회 (권한 체크 포함)
     * - 내 글이면 항상 조회 가능
     * - 남의 글이면 isPrivate=false 일 때만 조회 가능
     */
    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다. id=" + postId));

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        // 남의 글 + 비공개면 403
        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        if (!isOwner && post.isPrivate()) {
            throw new Exception403("해당 게시글을 조회할 권한이 없습니다.");
        }

        return new PostResponse(post);
    }

    /**
     * 게시글 수정 (작성자만 가능)
     */
    public PostResponse update(Long postId, PostUpdateRequest request, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다. id=" + postId));

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        // 작성자 여부 확인
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new Exception403("해당 게시글을 수정할 권한이 없습니다.");
        }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getTags(),
                request.isPrivate()
        );

        return new PostResponse(post);
    }

    /**
     * 게시글 삭제 (작성자만 가능)
     */
    public void delete(Long postId, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다. id=" + postId));

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new Exception403("해당 게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    /**
     * 공개 글 검색 (카테고리/태그/키워드 + 페이징)
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPublicPosts(
            String category,
            String keyword,
            String tag,
            Pageable pageable
    ) {
        Page<Post> page = postRepository.searchPublicPosts(category, keyword, tag, pageable);

        return page.map(PostResponse::new);
    }

}
