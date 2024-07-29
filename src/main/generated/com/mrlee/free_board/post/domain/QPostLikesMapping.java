package com.mrlee.free_board.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostLikesMapping is a Querydsl query type for PostLikesMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostLikesMapping extends EntityPathBase<PostLikesMapping> {

    private static final long serialVersionUID = -1826182827L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostLikesMapping postLikesMapping = new QPostLikesMapping("postLikesMapping");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final QPostLikesId id;

    public QPostLikesMapping(String variable) {
        this(PostLikesMapping.class, forVariable(variable), INITS);
    }

    public QPostLikesMapping(Path<? extends PostLikesMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostLikesMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostLikesMapping(PathMetadata metadata, PathInits inits) {
        this(PostLikesMapping.class, metadata, inits);
    }

    public QPostLikesMapping(Class<? extends PostLikesMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QPostLikesId(forProperty("id")) : null;
    }

}

