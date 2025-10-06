package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.constant.SenderType;
import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final Long SESSION_ID = 1L;
    private static final String USER_ID = "sanjay";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDateTime support
    }

    @Test
    void addMessage_ValidRequest_Success() throws Exception {
        MessageRequestDto requestDto = createMessageRequest("Hello, how are you?");
        MessageResponseDto responseDto = createMessageResponse("Hello, how are you?", SenderType.USER);

        when(messageService.addMessage(SESSION_ID, USER_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/sessions/{sessionId}/messages", SESSION_ID)
                        .header("X-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID));

        verify(messageService, times(1)).addMessage(SESSION_ID, USER_ID, requestDto);
    }

    @Test
    void addMessage_AssistantResponse_Success() throws Exception {
        MessageRequestDto requestDto = createMessageRequest("How are you doing?");
        MessageResponseDto responseDto = createMessageResponse( "I'm doing well, thank you!", SenderType.ASSISTANT);

        when(messageService.addMessage(SESSION_ID, USER_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/sessions/{sessionId}/messages", SESSION_ID)
                        .header("X-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID));

        verify(messageService, times(1)).addMessage(SESSION_ID, USER_ID, requestDto);
    }

    @Test
    void getMessageHistory_EmptyMessageList_Success() throws Exception {
        PageResponse<MessagesResponseDto> emptyPage = new PageResponse<>(null, 1, 100, 100, 10);
        when(messageService.getMessageHistory(SESSION_ID, 0, 50)).thenReturn(emptyPage);
        mockMvc.perform(get("/api/v1/sessions/{sessionId}/messages", SESSION_ID))
                .andExpect(status().isOk());
        verify(messageService, times(1)).getMessageHistory(SESSION_ID, 0, 50);
    }

    @Test
    void getMessageHistory_LargePaginationSize_Success() throws Exception {
        PageResponse<MessagesResponseDto> page = new PageResponse<>(null, 1, 100, 100, 10);
        when(messageService.getMessageHistory(SESSION_ID, 0, 100)).thenReturn(page);

        mockMvc.perform(get("/api/v1/sessions/{sessionId}/messages", SESSION_ID)
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk());

        verify(messageService, times(1)).getMessageHistory(SESSION_ID, 0, 100);
    }

    @Test
    void getMessageHistory_ConversationFlow_Success() throws Exception {
        PageResponse<MessagesResponseDto> page = new PageResponse<>(null, 0, 50, 0, 1);
        when(messageService.getMessageHistory(SESSION_ID, 0, 50)).thenReturn(page);

        mockMvc.perform(get("/api/v1/sessions/{sessionId}/messages", SESSION_ID))
                .andExpect(status().isOk());

        verify(messageService, times(1)).getMessageHistory(SESSION_ID, 0, 50);
    }

    @Test
    void addMessage_WithEmptyContent_Success() throws Exception {
        MessageRequestDto requestDto = createMessageRequest("");
        MessageResponseDto responseDto = createMessageResponse( "", SenderType.USER);

        when(messageService.addMessage(SESSION_ID, USER_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/sessions/{sessionId}/messages", SESSION_ID)
                        .header("X-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(messageService, times(1)).addMessage(SESSION_ID, USER_ID, requestDto);
    }

    @Test
    void addMessage_WithLongContent_Success() throws Exception {
        String longContent = "This is a very long message. ".repeat(50);
        MessageRequestDto requestDto = createMessageRequest(longContent);
        MessageResponseDto responseDto = createMessageResponse(longContent, SenderType.USER);

        when(messageService.addMessage(SESSION_ID, USER_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/sessions/{sessionId}/messages", SESSION_ID)
                        .header("X-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(messageService, times(1)).addMessage(SESSION_ID, USER_ID, requestDto);
    }

    private MessageRequestDto createMessageRequest(String content) {
        return MessageRequestDto.builder()
                .content(content)
                .senderType(SenderType.USER)
                .build();
    }

    private MessageResponseDto createMessageResponse(String content, SenderType senderType) {
        return MessageResponseDto.builder()
                .sessionId(SESSION_ID)
                .message(MessageResponse.builder().senderType(senderType).content(content).build()).build();
    }
}