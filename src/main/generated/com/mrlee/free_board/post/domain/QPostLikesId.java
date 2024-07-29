package com.mrlee.free_board.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostLikesId is a Querydsl query type for PostLikesId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPostLikesId extends BeanPath<PostLikesId> {

    private static final long serialVersionUID = -1938750412L;

    public static final QPostLikesId postLikesId = new QPostLikesId("postLikesId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public QPostLikesId(String variable) {
        super(PostLikesId.class, forVariable(variable));
    }

    public QPostLikesId(Path<? extends PostLikesId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostLikesId(PathMetadata metadata) {
        super(PostLikesId.class, metadata);
    }

}

