# free-board 포트폴리오(작성자 이경택)
## 프로젝트 주제
- 게시글, 댓글을 기능을 제공하는 소규모 게시판 프로그램

## 목차
- [프로젝트 상세](#프로젝트-상석세)
- [요구사항 분석](#요구사항-분석)
- [테이블 ERD](#테이블-ERD)
- [주요 기능 코드](#주요-기능-코드)

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
    - 실제 데이터를 삭제하지 않고, 숨김 상태로 변경한다.
  - 상세 조회
    - 해당 게시글의 조회수가 1만큼 오른다.
  - 목록 조회
    - 페이징 기능 적용
    - 생성일 오름차순 조회
    - 글 제목, 글 타입(FREE, TRAVEL, FOOD, SPORT)으로 조건 검색이 가능하다.
    - 본문의 길이가 100을 넘는 경우 뒷부분은 "..."로 표시.
    - 삭제된 글은 조회하지 않는다.
  - 좋아요 등록/삭제
    - post_likes_mapping 테이블에 데이터가 없는 경우 생성, 이미 존재하는 경우는 삭제한다. 
- 댓글 기능
  - 등록
  - 수정
  - 삭제
    - 실제 데이터를 삭제하지 않고, 숨김 상태로 변경한다.
  - 목록 조회
    - 페이징 기능 적용
    - 생성일 오름차순 조회
    - 게시글 작성자 여부
    - 좋아요 여부
    - 대댓글의 개수, 존재 여부 필드
  - 하위 댓글 등록
  - 하위 댓글 목록 조회
  - 좋아요 등록/삭제
    - comment_likes_mapping 테이블에 데이터가 없는 경우 생성, 이미 존재하는 경우는 삭제한다.
- 공통
  - 치명적 예외 발생시와 API 응답 시간 1.5초 이상인 경우 관리자에게 알림 메일 전송

### 테이블 ERD
<img width="800" alt="free_board_ERD1" src="https://github.com/user-attachments/assets/b9e27313-f564-49a3-9d3d-edf89bbe6013">

### API 기능 소개
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
            "commentCount": 650,
            "type": "FREE",
            "author": "사용자1",
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
#### 댓글 목록 조회
#### 대댓글 목록 조회
#### API 응답 시간 1.5초 이상인 경우 관리자에게 메일 알림(스프링AOP)
