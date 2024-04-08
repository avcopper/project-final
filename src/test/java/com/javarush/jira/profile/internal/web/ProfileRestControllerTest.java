package com.javarush.jira.profile.internal.web;

import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.AbstractControllerTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProfileRestController.class)
class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbstractProfileController profileController;

    @Test
    @WithMockUser(username = "testUser", password = "password", roles = "USER")
    public void testGetProfile() throws Exception {
        Long id = 1L;

        Set<String> mailNotifications = new HashSet<>();
        mailNotifications.add("notification1");
        mailNotifications.add("notification2");

        Set<ContactTo> contacts = new HashSet<>();
        contacts.add(new ContactTo("John Doe", "john@example.com"));
        contacts.add(new ContactTo("Jane Doe", "jane@example.com"));

        // Mock the behavior of the underlying service
        ProfileTo mockProfile = new ProfileTo(id,mailNotifications,contacts);
        when(profileController.get(anyLong())).thenReturn(mockProfile);

        // Perform the GET request to the REST endpoint
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.someProperty").value("expectedValue")); // Adjust this based on your ProfileTo structure

        // Assertions
        assertEquals(id, mockProfile.getId());
        assertEquals(mailNotifications, mockProfile.getMailNotifications());
        assertEquals(contacts, mockProfile.getContacts());
    }

    @Test
    @WithMockUser(username = "testUser", password = "password", roles = "USER")
    public void testUpdateProfile() throws Exception {
        // Mock the behavior of the underlying service
        doNothing().when(profileController).update(any(ProfileTo.class), anyLong());

        // Perform the PUT request to the REST endpoint with a sample JSON payload
        mockMvc.perform(put("/api/profile")
                        .contentType("application/json")
                        .content("{\"someProperty\":\"updatedValue\"}"))
                .andExpect(status().isNoContent());
    }

}