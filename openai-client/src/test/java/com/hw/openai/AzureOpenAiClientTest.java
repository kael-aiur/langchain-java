/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hw.openai;

import com.hw.openai.entity.chat.ChatCompletion;
import com.hw.openai.entity.chat.Message;
import com.hw.openai.entity.completions.Completion;
import com.hw.openai.entity.embeddings.Embedding;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href="https://platform.openai.com/docs/api-reference/completions">OpenAI API reference</a>
 *
 * @author Tingliang Wang
 */
@Disabled("Test requires costly Azure OpenAI calls, can be run manually.")
class AzureOpenAiClientTest {

    private static OpenAiClient client;

    @BeforeAll
    static void setup() {
        client = OpenAiClient.builder()
                .openaiApiKey("xxx")
                .openaiApiType("azure")
                .openaiApiBase("https://xxx.openai.azure.com/")
                .openaiApiVersion("2023-05-15")
                .build()
                .init();
    }

    @AfterAll
    static void cleanup() {
        client.close();
    }

    @Test
    void testCompletion() {
        Completion completion = Completion.builder()
                .model("text-davinci-003")
                .prompt(List.of("Say this is a test"))
                .maxTokens(700)
                .temperature(0)
                .build();

        assertThat(client.completion(completion)).isEqualTo("This is indeed a test.");
    }

    @Test
    void testChatCompletion() {
        Message message = Message.of("Hello!");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model("gpt-35-turbo")
                .temperature(0)
                .messages(List.of(message))
                .build();

        assertThat(client.chatCompletion(chatCompletion)).isEqualTo("Hello there! How can I assist you today?");
    }

    @Test
    void testEmbeddings() {
        var embedding = Embedding.builder()
                .model("text-embedding-ada-002")
                .input(List.of("The food was delicious and the waiter..."))
                .build();

        var response = client.embedding(embedding);

        assertThat(response).as("Response should not be null").isNotNull();
        assertThat(response.getData()).as("Data list should have size 1").hasSize(1);
        assertThat(response.getData().get(0).getEmbedding()).as("Embedding should have size 1536").hasSize(1536);
    }
}