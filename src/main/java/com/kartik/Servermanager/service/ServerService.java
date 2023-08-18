package com.kartik.Servermanager.service;

import com.kartik.Servermanager.model.Server;

import java.util.Collection;


public interface ServerService {
    Server create(Server server);
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
    Server ping(String ipAddress);
}
