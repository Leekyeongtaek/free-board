package com.mrlee.free_board.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentLikesId is a Querydsl query type for CommentLikesId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommentLikesId extends BeanPath<CommentLikesId> {

    private static final long serialVersionUID = -924060037L;

    public static final QCommentLikesId commentLikesId = new QCommentLikesId("commentLikesId");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QCommentLikesId(String variable) {
        super(CommentLikesId.class, forVariable(variable));
    }

    public QCommentLikesId(Path<? extends CommentLikesId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentLikesId(PathMetadata metadata) {
        super(CommentLikesId.class, metadata);
    }

}

