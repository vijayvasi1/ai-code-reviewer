package com.vijay.codereviewer.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaLLMProvider implements LLMProvider {

    private final RestClient restClient;
    private final String model;

    public OllamaLLMProvider(
            @Value("${llm.ollama.base-url}") String baseUrl,
            @Value("${llm.ollama.model}") String model) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();

        this.model = model;
    }

    @Override
    public String generate(String prompt) {

        OllamaRequest request = new OllamaRequest(
                model,
                List.of(
                        new Message("user", prompt)
                ),
                false
        );

        OllamaResponse response = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaResponse.class);

        if (response == null || response.message() == null) {
            throw new IllegalStateException(
                    "No response received from Ollama"
            );
        }

        return response.message().content();
    }

    public record OllamaRequest(
            String model,
            List<Message> messages,
            boolean stream
    ) {
    }

    public record Message(
            String role,
            String content
    ) {
    }

    public record OllamaResponse(
            String model,
            Message message,
            boolean done
    ) {
    }
}
