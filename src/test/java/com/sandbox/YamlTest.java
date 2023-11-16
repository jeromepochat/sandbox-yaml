package com.sandbox;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.utils.Serialization;

public class YamlTest {

    @Test
    public void parseUsingFabric8() throws IOException {
        try (var input = getClass().getClassLoader().getResourceAsStream("pod.yaml")) {
            var pod = Serialization.unmarshal(input, Pod.class);
            var port = pod.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();

            assertEquals(80, port);
        }
    }

    @Test
    public void parseUsingSnake20() throws IOException {
        try (var input = getClass().getClassLoader().getResourceAsStream("pod.yaml")) {
            var yaml = new Yaml();
            var pod = yaml.loadAs(input, Pod.class);
            var port = pod.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();

            assertEquals(80, port);
        }
    }

    @Test
    public void parseUsingJackson() throws IOException {
        try (var input = getClass().getClassLoader().getResourceAsStream("pod.yaml")) {
            var mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();

            var pod = mapper.readValue(input, Pod.class);
            var port = pod.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort();
            assertEquals(80, port);
        }
    }
}
