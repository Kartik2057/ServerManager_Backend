package com.kartik.Servermanager.service.implementation;

import com.kartik.Servermanager.model.Server;
import com.kartik.Servermanager.repo.ServerRepo;
import com.kartik.Servermanager.service.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.kartik.Servermanager.enumeration.Status.SERVER_DOWN;
import static com.kartik.Servermanager.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;
import static org.springframework.data.querydsl.QPageRequest.of;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImplementation implements ServerService {


    private final ServerRepo serverRepo;
    @Override
    public Server create(Server server) {
        log.info("Saving new Server:{}",server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Finding all servers");
        return serverRepo.findAll(of(0,limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}",id);
        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server : {}",id);
        serverRepo.deleteById(id);
        return TRUE;
    }

    @Override
    public Server ping(String ipAddress) {
        log.info("Pinging server Ip:{}",ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);

        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            server.setStatus(address.isReachable(10000)? SERVER_UP: SERVER_DOWN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverRepo.save(server);
        return server;
    }

    private String setServerImageUrl() {
        String[] imageName = {"server1.png","server2.png","server3.png","server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageName[new Random().nextInt(4)]).toUriString();
    }
}
