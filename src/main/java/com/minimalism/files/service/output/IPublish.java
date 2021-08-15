package com.minimalism.files.service.output;

import java.util.List;

import com.minimalism.files.domain.entities.Entity;

public interface IPublish {
    void publish(List<Entity> records) throws InterruptedException;
}
