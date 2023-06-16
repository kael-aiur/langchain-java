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

package com.hw.langchain.chat.models.base;

import com.hw.langchain.base.language.BaseLanguageModel;
import com.hw.langchain.schema.*;

import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author HamaWhite
 */
@SuperBuilder
public abstract class BaseChatModel implements BaseLanguageModel {

    /**
     * Tags to add to the run trace.
     */
    private List<String> tags;

    public LLMResult generate(List<List<BaseMessage>> messages, List<String> stop) {
        return generate(messages, stop, null);
    }

    /**
     * Top Level call
     */
    public LLMResult generate(List<List<BaseMessage>> messages, List<String> stop, List<String> tags) {

        return null;
    }

    @Override
    public LLMResult generatePrompt(List<PromptValue> prompts, List<String> stop) {
        List<List<BaseMessage>> promptMessages = prompts.stream()
                .map(PromptValue::toMessages)
                .toList();
        return generate(promptMessages, stop);

    }

    public BaseMessage call(List<BaseMessage> messages, List<String> stop) {
        var generation = generate(List.of(messages), stop).getGenerations().get(0).get(0);
        if (generation instanceof ChatGeneration chatGeneration) {
            return chatGeneration.getMessage();
        } else {
            throw new IllegalArgumentException("Unexpected generation type");
        }
    }

    @Override
    public String predict(String text, List<String> stop) {
        List<String> copyStop = stop != null ? List.copyOf(stop) : null;
        BaseMessage message = new HumanMessage(text);

        BaseMessage result = call(List.of(message), copyStop);
        return result.getContent();
    }

    @Override
    public BaseMessage predictMessages(List<BaseMessage> messages, List<String> stop) {
        List<String> copyStop = stop != null ? List.copyOf(stop) : null;
        return call(messages, copyStop);
    }
}
