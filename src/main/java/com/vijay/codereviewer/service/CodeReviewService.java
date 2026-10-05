package com.vijay.codereviewer.service;

import com.vijay.codereviewer.llm.LLMProvider;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewService {

    private final LLMProvider llmProvider;

    public CodeReviewService(LLMProvider llmProvider) {
        this.llmProvider = llmProvider;
    }

    public String review(String code) {

        String prompt = """
                You are a senior software engineer performing
                a professional code review.

                Analyze the following source code for:

                1. Bugs
                2. Security vulnerabilities
                3. Performance problems
                4. Code quality issues
                5. Edge cases
                6. Error handling
                7. Maintainability
                8. Input validation
                9. Missing test cases

                Rules:

                - Do not invent issues.
                - Only report issues supported by the code.
                - Explain why each issue matters.
                - Provide a recommendation.
                - Assign severity:
                  LOW, MEDIUM, HIGH, CRITICAL.

                Source code:
                ----------------------------

                %s

                ----------------------------
                """.formatted(code);

        return llmProvider.generate(prompt);
    }
}