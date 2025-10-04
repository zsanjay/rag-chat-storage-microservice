package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.service.SessionService;
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
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private static final Long SESSION_ID = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createSession_Success() throws Exception {

        SessionRequestDto sessionRequestDto = SessionRequestDto.builder()
                .userId("user1")
                .title("Test Session")
                .isFavorite(false)
                .build();
        
        SessionResponseDto responseDto = SessionResponseDto.builder()
                .isFavorite(false)
                .sessionId(SESSION_ID)
                .title("Test Session")
                .build();
        
        when(sessionService.createSession(sessionRequestDto)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/sessions/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.title").value("Test Session"))
                .andExpect(jsonPath("$.isFavorite").value(false));

        verify(sessionService, times(1)).createSession(sessionRequestDto);
    }


    @Test
    void getSessions_DefaultPagination_Success() throws Exception {
        SessionResponseDto session1 = createSessionResponse(SESSION_ID, "Session 1", false);
        SessionResponseDto session2 = createSessionResponse(2L, "Session 2", true);
        List<SessionResponseDto> sessions = Arrays.asList(session1, session2);
        PagedResult<List<SessionResponseDto>> pagedResult = new PagedResult<>();
        pagedResult.setContent(sessions);

        when(sessionService.getSessions(0, 10)).thenReturn(pagedResult);

        mockMvc.perform(get("/api/v1/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.content[0].title").value("Session 1"))
                .andExpect(jsonPath("$.content[1].sessionId").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("Session 2"))
                .andExpect(jsonPath("$.content[1].isFavorite").value(true));

        verify(sessionService, times(1)).getSessions(0, 10);
    }

    @Test
    void getSessions_CustomPagination_Success() throws Exception {
        List<SessionResponseDto> sessions = Arrays.asList(createSessionResponse(SESSION_ID, "Session 1", false));
        PagedResult<List<SessionResponseDto>> pagedResult = new PagedResult<>();
        pagedResult.setContent(sessions);

        when(sessionService.getSessions(2, 5)).thenReturn(pagedResult);

        mockMvc.perform(get("/api/v1/sessions")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(sessionService, times(1)).getSessions(2, 5);
    }

    @Test
    void getSession_ValidSessionId_Success() throws Exception {
        SessionResponseDto responseDto = createSessionResponse(SESSION_ID, "Test Session", false);

        when(sessionService.getSession(SESSION_ID)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/sessions/{sessionId}", SESSION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.title").value("Test Session"));

        verify(sessionService, times(1)).getSession(SESSION_ID);
    }


    @Test
    void updateSessionTitle_ValidRequest_Success() throws Exception {
        TitleChangeRequestDto requestDto = new TitleChangeRequestDto("Updated Title");
        SessionResponseDto responseDto = createSessionResponse(SESSION_ID, "Updated Title", false);

        when(sessionService.updateSessionTitle(SESSION_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/sessions/{sessionId}", SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(sessionService, times(1)).updateSessionTitle(SESSION_ID, requestDto);
    }


    @Test
    void updateFavorite_MarkAsFavorite_Success() throws Exception {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(true);
        SessionResponseDto responseDto = createSessionResponse(SESSION_ID, "Test Session", true);

        when(sessionService.updateFavorite(SESSION_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/favorite", SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID))
                .andExpect(jsonPath("$.isFavorite").value(true));

        verify(sessionService, times(1)).updateFavorite(SESSION_ID, requestDto);
    }

    @Test
    void updateFavorite_UnmarkAsFavorite_Success() throws Exception {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(false);
        SessionResponseDto responseDto = createSessionResponse(SESSION_ID, "Test Session", false);

        when(sessionService.updateFavorite(SESSION_ID, requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/favorite", SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFavorite").value(false));

        verify(sessionService, times(1)).updateFavorite(SESSION_ID, requestDto);
    }

    @Test
    void deleteSession_ValidSessionId_Success() throws Exception {
        doNothing().when(sessionService).deleteSession(SESSION_ID);

        mockMvc.perform(delete("/api/v1/sessions/{sessionId}", SESSION_ID))
                .andExpect(status().isNoContent());

        verify(sessionService, times(1)).deleteSession(SESSION_ID);
    }

    private SessionResponseDto createSessionResponse(Long id, String title, boolean favorite) {
        return SessionResponseDto.builder()
                .sessionId(id)
                .title(title)
                .isFavorite(favorite)
                .build();
    }
}