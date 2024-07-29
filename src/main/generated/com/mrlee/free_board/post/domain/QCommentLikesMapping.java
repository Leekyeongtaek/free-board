package com.mrlee.free_board.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentLikesMapping is a Querydsl query type for CommentLikesMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLikesMapping extends EntityPathBase<CommentLikesMapping> {

    private static final long serialVersionUID = -2133128338L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentLikesMapping commentLikesMapping = new QCommentLikesMapping("commentLikesMapping");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final QCommentLikesId id;

    public QCommentLikesMapping(String variable) {
        this(CommentLikesMapping.class, forVariable(variable), INITS);
    }

    public QCommentLikesMapping(Path<? extends CommentLikesMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentLikesMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentLikesMapping(PathMetadata metadata, PathInits inits) {
        this(CommentLikesMapping.class, metadata, inits);
    }

    public QCommentLikesMapping(Class<? extends CommentLikesMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QCommentLikesId(forProperty("id")) : null;
    }

}

