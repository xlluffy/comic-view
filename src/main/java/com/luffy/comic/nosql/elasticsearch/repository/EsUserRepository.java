/*
package com.luffy.comic.nosql.elasticsearch.repository;

import com.luffy.comic.nosql.elasticsearch.document.EsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface EsUserRepository extends ElasticsearchRepository<EsUser, Long> {
    List<EsUser> findByUsernameOrNickName(String username, String nickName);

    List<EsUser> findByEmail(String email);

    List<EsUser> findByCreateTimeOrLoginTime(Date createTime, Date loginTime);

    List<EsUser> findByStatus(int status);
}
*/
