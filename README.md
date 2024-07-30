# free-board 포트폴리오(작성자 이경택)
## 프로젝트 주제
- 게시글, 댓글을 기능을 제공하는 소규모 게시판 프로그램

## 목차
- [프로젝트 상세](#프로젝트-상석세)
- [요구사항 분석](#요구사항-분석)
- [기능 요구사항 상세](#기능-요구사항-상세)
- [테이블 ERD](#테이블-ERD)
- [API 코드](#API-코드)

### 프로젝트 상세
- 개발환경 : 인텔리제이
- 자바 : 17
- 스프링부트 : 3.3.1
- MySQL : 8.0.32

### 요구사항 분석
- 회원 기능
  - 회원가입
- 게시글 기능
  - 등록
  - 수정
  - 삭제
  - 상세 조회
  - 목록 조회
  - 좋아요 등록/삭제
- 댓글 기능
  - 등록
  - 수정
  - 삭제
  - 목록 조회
  - 대댓글 등록
  - 대댓글 목록 조회
  - 좋아요 등록/삭제
- 공통
  - 치명적 예외 발생시 또는 API 응답 시간 1.5초 이상인 경우 관리자에게 알림 메일 전송

### 기능 요구사항 상세
- 게시글 기능
  - 삭제
    - 데이터를 실제로 삭제하지 않고, 숨김 상태로 변경
  - 상세 조회
    - 해당 게시글의 조회수가 1만큼 오름
  - 목록 조회
    - 페이징 기능
    - 글의 제목, 타입으로 검색 가능
    - 본문의 길이가 100을 넘는 경우 뒷부분은 "..."로 표시
    - 삭제된 글은 조회하지 않음
  - 좋아요 등록/삭제
    - post_likes_mapping 테이블에 데이터가 없는 경우 생성하고, 이미 존재하는 경우는 삭제
- 댓글 기능
  - 목록 조회
    - 페이징 기능
    - 게시글 작성자 여부를 나타내는 필드 포함
    - 좋아요 여부를 나타내는 필드 포함
    - 대댓글의 개수를 나타내는 필드 포함
    - 삭제한 댓글은 "삭제된 댓글입니다"로 표시
  - 삭제  
    - 실제 데이터를 삭제하지 않고, 숨김 상태로 변경
  - 좋아요 등록/삭제
    - comment_likes_mapping 테이블에 데이터가 없는 경우 생성, 이미 존재하는 경우는 삭제

### 테이블 ERD
<img width="800" alt="free_board_ERD1" src="https://github.com/user-attachments/assets/b9e27313-f564-49a3-9d3d-edf89bbe6013">

### API 코드
#### 게시글 등록
```
요청 데이터 예시
  {
      "title" : "게시글의 제목",
      "content" : "본문 내용",
      "memberId" : "1",
      "type" : "FREE" //(FREE, TRAVEL, FOOD, SPORT) 타입 존재
  }

컨트롤러 계층
  @PostMapping
  public ResponseEntity<Void> postAdd(@RequestBody PostSaveForm form) {
      postService.addPost(form);
      return new ResponseEntity<>(OK);
  }

서비스 계층
  public void addPost(PostSaveForm form) {
    Member member = memberRepository.findById(form.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 정보를 찾을 수 없습니다."));
    Post post = form.toPostEntity(member);
    postRepository.save(post);
  }
```
#### 게시글 상세 조회
```
응답 데이터 예시
{
    "postId": "게시글 번호",
    "memberId": 1,
    "title": "게시글 제목",
    "content": "본문 내용",
    "author": "사용자1", //작성자 닉네임
    "views": 2, //조회수
    "likes": 0, //좋아요 개수
    "likeYn": true, //로그인 회원의 해당 게시글 좋아요 여부
    "createdDate": "2024-07-30T20:01:05"
}

컨트롤러 계층
  @GetMapping("/{postId}")
  public ResponseEntity<PostQueryDto> postDetails(@PathVariable(name = "postId") Long postId, @RequestParam(name = "loginMemberId") Long        loginMemberId) {
      PostQueryDto postQueryDto = postService.findPost(postId, loginMemberId);
      return new ResponseEntity<>(postQueryDto, OK);
  }

서비스 계층
  public PostQueryDto findPost(Long postId, Long loginMemberId) {
    return postQueryRepository.findPost(postId, loginMemberId);
  }

저장소 계층
  public PostQueryDto findPost(Long postId, Long loginMemberId) {

        PostQueryDto postQueryDto = queryFactory.select(new QPostQueryDto(post, member, postLikesMapping.isNotNull()))
                .from(post)
                .join(post.member, member)
                .leftJoin(postLikesMapping).on(post.id.eq(postLikesMapping.id.postId)
                        .and(postLikesMapping.id.memberId.eq(loginMemberId)))
                .where(post.id.eq(postId))
                .fetchOne();

        if (postQueryDto == null) {
            throw new IllegalArgumentException("해당하는 글을 찾을 수 없습니다.");
        }

        queryFactory.update(post)
                .set(post.views, post.views.add(1))
                .where(post.id.eq(postId))
                .execute();

        return postQueryDto;
    }
```
#### 게시글 목록 조회
```
응답 데이터 예시
{
    "totalPages": 1000,
    "totalElements": 20000,
    "first": true,
    "last": false,
    "size": 20,
    "content": [
        {
            "postId": 1,
            "title": "게시글 제목",
            "content": "게시글 내용",
            "views": 1,
            "likes": 0,
            "commentCount": 650, //댓글의 개수
            "type": "FREE",
            "author": "사용자1", //댓글 작성 회원의 닉네임
            "createdDate": "2024-07-30T20:01:05"
        },
        {
            "postId": 2,
            "title": "게시글 제목",
            "content": "게시글 내용",
            "views": 0,
            "likes": 0,
            "commentCount": 50,
            "type": "FREE",
            "author": "사용자1",
            "createdDate": "2024-07-30T20:01:05"
        },
        {
            "postId": 3,
            "title": "게시글 제목",
            "content": "게시글 내용",
            "views": 0,
            "likes": 0,
            "commentCount": 50,
            "type": "FREE",
            "author": "사용자1",
            "createdDate": "2024-07-30T20:01:05"
        } ...
    ],
    "number": 0,
    "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
    },
    "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "numberOfElements": 20,
    "empty": false
}

컨트롤러 계층
  @GetMapping
  public ResponseEntity<Page<PostQueryDtoList>> postList(Pageable pageable, @ModelAttribute PostSearchForm form) throws
  InterruptedException {
        Page<PostQueryDtoList> posts = postService.searchPosts(pageable, form);
        return new ResponseEntity<>(posts, OK);
  }

서비스 계층
  public Page<PostQueryDtoList> searchPosts(Pageable pageable, PostSearchForm form) throws InterruptedException {
        return postQueryRepository.searchPosts(pageable, form);
    }

저장소 계층
  public Page<PostQueryDtoList> searchPosts(Pageable pageable, PostSearchForm form) {

      List<PostQueryDtoList> postQueryDtoLists = queryFactory
              .select(Projections.constructor(PostQueryDtoList.class,
                      post.id, post.title, post.content,
                      post.views, post.likes, post.type,
                      member.nickName, post.createdDate, post.commentList.size()))
              .from(post)
              .join(post.member, member)
              .where(
                      postTitleContains(form.getTitle()),
                      postTypeEq(form.getType()),
                      post.hiddenYn.eq(false))
              .offset(pageable.getOffset())
              .limit(pageable.getPageSize())
              .fetch();

      Long totalCount = queryFactory
              .select(post.count())
              .from(post)
              .where(
                      postTitleContains(form.getTitle()),
                      postTypeEq(form.getType()),
                      post.hiddenYn.eq(false))
              .fetchOne();

      return new PageImpl<>(postQueryDtoLists, pageable, totalCount);
  }

  private BooleanExpression postTitleContains(String title) {
    return StringUtils.hasText(title) ? post.title.contains(title) : null;
  }

  private BooleanExpression postTypeEq(PostType type) {
      return type != null ? post.type.eq(type) : null;
  }
```
#### 게시글 삭제
```
컨트롤러 계층
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> postRemove(@PathVariable(name = "postId") Long postId) {
    postService.removePost(postId);
    return new ResponseEntity<>(OK);
  }

서비스 계층
  public void removePost(Long postId) {
    Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
    post.hide();
  }

  @Entity
  public class Post extends AuditingTime {
    ...
    private boolean hiddenYn; //숨김 여부 필드

    public void hide() {
        this.hiddenYn = true;
    }
  }
```
#### 게시글 좋아요
```
컨트롤러 계층
  @PostMapping("/{postId}/like")
    public ResponseEntity<Void> requestCommentLike(@PathVariable(name = "postId") Long postId, @RequestBody PostLikesSaveForm form) {
      postService.requestPostLike(postId, form);
      return new ResponseEntity<>(OK);
  }

서비스 계층
  public void requestPostLike(Long postId, PostLikesSaveForm form) {
    Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
    PostLikesId postLikesId = new PostLikesId(postId, form.getMemberId());
    Optional<PostLikesMapping> postLikesMapping = postLikesMappingRepository.findById(postLikesId);
    if (postLikesMapping.isPresent()) {
        postLikesMappingRepository.delete(postLikesMapping.get());
        post.minusLikes();
    } else {
        postLikesMappingRepository.save(new PostLikesMapping(postLikesId));
        post.plusLikes();
    }
  }

  @Entity
  public class Post extends AuditingTime {
    ...
    public void plusLikes() {
        this.likes++;
    }
    public void minusLikes() {
        this.likes--;
    }
  }
```
#### 댓글 목록 조회
```
응답 데이터 예시
{
    "totalPages": 3,
    "totalElements": 50,
    "first": true,
    "last": false,
    "size": 20,
    "content": [
       {
            "commentId": 1,
            "memberId": 5,
            "author": "사용자5",
            "content": "부모 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:23:55",
            "hasChild": true, //대댓글이 있는 지 여부
            "childCommentCount": 30, //대댓글의 개수
            "authorYn": false, //게시글의 작성자가 작성한 댓글인지 여부
            "likesYn": false //현재 로그인한 회원이 해당 댓글을 좋아요 했는지 여부 
        },
        {
            "commentId": 2,
            "memberId": 5,
            "author": "사용자5",
            "content": "부모 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:23:55",
            "hasChild": true,
            "childCommentCount": 30,
            "authorYn": false,
            "likesYn": false
        },
        {
            "commentId": 3,
            "memberId": 5,
            "author": "사용자5",
            "content": "부모 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:23:55",
            "hasChild": true,
            "childCommentCount": 30,
            "authorYn": false,
            "likesYn": false
        },
        {
            "commentId": 4,
            "memberId": 5,
            "author": "사용자5",
            "content": "부모 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:23:55",
            "hasChild": true,
            "childCommentCount": 30,
            "authorYn": false,
            "likesYn": false
        },
        ...
    ],
    "number": 0,
    "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
    },
    "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "numberOfElements": 20,
    "empty": false
}

컨트롤러 계층
  @GetMapping("/{postId}/comments")
  public ResponseEntity<Page<CommentQueryDtoList>> parentCommentList(@PathVariable(name = "postId") Long postId, @RequestParam(name =  
  "loginMemberId") Long loginMemberId,Pageable pageable) {
      Page<CommentQueryDtoList> result = postService.findParentComments(postId, loginMemberId, pageable);
      return new ResponseEntity<>(result, OK);
  }

서비스 계층
  public Page<CommentQueryDtoList> findParentComments(Long postId, Long loginMemberId, Pageable pageable) {
    return commentQueryRepository.findParentComments(postId, loginMemberId, pageable);
  }

저장소 계층
  public Page<CommentQueryDtoList> findParentComments(Long postId, Long loginMemberId, Pageable pageable) {
  
    QComment childComment = new QComment("childComment");
    
    List<CommentQueryDtoList> commentQueryDtoList = queryFactory
            .select(new QCommentQueryDtoList(comment.id, comment.content, comment.likes,
                    comment.createdDate, comment.hiddenYn, member.id, member.nickName,
                    commentLikesMapping.isNotNull(), childComment.count(), post.member.id))
            .from(comment)
            .join(comment.member, member)
            .join(comment.post, post)
            .leftJoin(commentLikesMapping).on(commentLikesMapping.id.commentId.eq(comment.id)
                    .and(commentLikesMapping.id.memberId.eq(loginMemberId)))
            .leftJoin(childComment).on(childComment.parentComment.id.eq(comment.id))
            .where(
                    comment.post.id.eq(postId),
                    comment.parentComment.id.isNull())
            .groupBy(comment.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    
    Long totalCount = queryFactory.select(comment.count())
            .from(comment)
            .where(
                    comment.post.id.eq(postId),
                    comment.parentComment.id.isNull())
            .fetchOne();
    
    return new PageImpl<>(commentQueryDtoList, pageable, totalCount);
  }
```
#### 대댓글 목록 조회
```
응답 데이터 예시
{
    "totalPages": 2,
    "totalElements": 30,
    "first": true,
    "last": false,
    "size": 20,
    "content": [
        {
            "commentId": 591,
            "parentCommentId": 4,
            "memberId": 8,
            "author": "사용자8",
            "content": "하위 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:48:10",
            "likesYn": false,
            "authorYn": false
        },
        {
            "commentId": 592,
            "parentCommentId": 4,
            "memberId": 8,
            "author": "사용자8",
            "content": "하위 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:48:10",
            "likesYn": false,
            "authorYn": false
        },
        {
            "commentId": 593,
            "parentCommentId": 4,
            "memberId": 8,
            "author": "사용자8",
            "content": "하위 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:48:10",
            "likesYn": false,
            "authorYn": false
        },
        {
            "commentId": 594,
            "parentCommentId": 4,
            "memberId": 8,
            "author": "사용자8",
            "content": "하위 댓글",
            "likes": 0,
            "createdDate": "2024-07-30T20:48:10",
            "likesYn": false,
            "authorYn": false
        },
        ...
    ],
    "number": 0,
    "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
    },
    "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "numberOfElements": 20,
    "empty": false
}

컨트롤러 계층
  @GetMapping("/comments/{commentId}")
  public ResponseEntity<Page<ChildCommentQueryDto>> childCommentList(@PathVariable(name = "commentId") Long commentId,
    @RequestParam(name = "loginMemberId") Long loginMemberId,Pageable pageable) {
      Page<ChildCommentQueryDto> childComments = postService.findChildComments(commentId, pageable, loginMemberId);
      return new ResponseEntity<>(childComments, OK);
  }

서비스 계층
  public Page<ChildCommentQueryDto> findChildComments(Long commentId, Pageable pageable, Long loginMemberId) {
    return commentQueryRepository.findChildComments(pageable, commentId, loginMemberId);
  }

저장소 계층
  public Page<ChildCommentQueryDto> findChildComments(Pageable pageable, Long commentId, Long loginMemberId) {

    List<ChildCommentQueryDto> childCommentQueryDtoList = queryFactory
            .select(new QChildCommentQueryDto(
                    comment.id, comment.parentComment.id, member.id,
                    member.nickName, comment.likes, comment.createdDate, comment.content,
                    comment.hiddenYn, commentLikesMapping.isNotNull(), post.member.id))
            .from(comment)
            .join(comment.member, member)
            .join(comment.post, post)
            .leftJoin(commentLikesMapping).on(commentLikesMapping.id.commentId.eq(comment.id)
                    .and(commentLikesMapping.id.memberId.eq(loginMemberId)))
            .where(
                    comment.parentComment.id.eq(commentId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
  
    Long totalCount = queryFactory.select(comment.count())
            .from(comment)
            .where(
                    comment.parentComment.id.eq(commentId))
            .fetchOne();
  
    return new PageImpl<>(childCommentQueryDtoList, pageable, totalCount);
  }
```
#### API 응답 시간 1.5초 이상인 경우 관리자에게 메일 알림(스프링AOP)
<img width="800" alt="스크린샷 2024-07-31 오전 3 18 08" src="https://github.com/user-attachments/assets/95bb2905-e214-4e09-ae43-932cbe3cd892">

```
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class MyAspect {

  private final MyMailSender myMailSender;

  @Around(value = "execution(* com.mrlee.free_board.post.service.PostService.*(..))")
  public Object checkExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
      long startTime = System.currentTimeMillis();
      Object result = joinPoint.proceed();
      long endTime = System.currentTimeMillis();
      long executionTime = endTime - startTime;
      checkExecutionTime(executionTime, joinPoint);
      return result;
  }

  private void checkExecutionTime(long executionTime, ProceedingJoinPoint joinPoint) {
      if (executionTime > 1500) {
          log.info("실행 시간 = {}", executionTime);
          log.info("실행 메서드 = {}", joinPoint.getSignature().toShortString());
          myMailSender.sendMail(MyMailMessage.LowQualityMailMessage);
      }
  }
}

@RequiredArgsConstructor
@Component
public class MyMailSender {

  private final JavaMailSender javaMailSender;

  private final static String receiver = "lkt0520@naver.com";

  public void sendMail(MyMailMessage myMailMessage) {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setSubject(myMailMessage.getSubject());
      simpleMailMessage.setText(myMailMessage.getText());
      simpleMailMessage.setTo(receiver);
      javaMailSender.send(simpleMailMessage);
  }
}

@Getter
@RequiredArgsConstructor
public enum MyMailMessage {

  CriticalMailMessage("[치명적 오류 발생 알림] 자유 게시판 관리자 확인 요망", "서버 로그를 확인 해주시기 바랍니다."),
  LowQualityMailMessage("[매우 늦은 쿼리 발생 알림] API 응답 속도가 매우 느림 관리자 확인 요망", "서버 로그를 확인 해주시기 바랍니다.");

  MyMailMessage(String subject, String text) {
      this.subject = subject;
      this.text = text;
  }

  private String subject;
  private String text;
}
```
