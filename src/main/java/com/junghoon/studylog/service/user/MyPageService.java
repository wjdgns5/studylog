package com.junghoon.studylog.service.user;

import com.junghoon.studylog.domain.user.User;
import com.junghoon.studylog.dto.user.MyPageResponse;
import com.junghoon.studylog.global.error.Exception400;
import com.junghoon.studylog.repository.post.PostRepository;
import com.junghoon.studylog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public MyPageResponse getMyPage(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        long total = postRepository.countByAuthor(user);
        long publicCount = postRepository.countByAuthorAndIsPrivateFalse(user);
        long privateCount = postRepository.countByAuthorAndIsPrivateTrue(user);

        return MyPageResponse.builder()
                .email(user.getEmail())
                .joinedAt(user.getCreatedAt())
                .totalPosts(total)
                .publicPosts(publicCount)
                .privatePosts(privateCount)
                .build();
    }
}