package com.minimalism.files.service.output;

import java.util.List;

import com.minimalism.files.domain.entities.InputEntity;

public interface IPublish {
    void publish(List<InputEntity> records, boolean asGeneric) throws InterruptedException;
}
