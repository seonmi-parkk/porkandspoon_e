package kr.co.porkandspoon.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class ApprovalRaceConditionTest {

    @Autowired
    private MockMvc mockMvc;
    private static final Logger log = LoggerFactory.getLogger(ApprovalRaceConditionTest.class);


    @Test
    void simpleLogTest() {
        log.info("✅ 로그 테스트입니다");
    }

    @Test
    void testRecallAndApproveRaceCondition() throws Exception {
        String docId = "221";

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        Runnable recallTask = () -> {
            try {
                System.out.println("✅ 회수 시작: " + System.nanoTime());
                MvcResult result = mockMvc.perform(put("/approval/recall/" + docId)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf()))
                        .andReturn();
                String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                System.out.println("✅ 회수 응답 본문: " + body);

                int status = result.getResponse().getStatus();
                System.out.println("✅ 회수 응답 상태: " + status);
            } catch (Exception e) {
                log.info("❌ 회수 중 예외 발생");
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };

        Runnable approveTask = () -> {
            try {
                System.out.println("✅ 결재 시작: " + System.nanoTime());
                MvcResult result = mockMvc.perform(post("/approval/ApprovalDraft")
                                .param("draft_idx", docId)
                                .with(user("test50").roles("USER"))
                                .with(csrf()))
                        .andReturn();
                String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                System.out.println("✅ 결재 응답 본문: " + body);

                int status = result.getResponse().getStatus();
                System.out.println("✅ 결재 응답 상태: " + status);
            } catch (Exception e) {
                log.info("❌ 결재 중 예외 발생");
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };

        executor.submit(recallTask);
        executor.submit(approveTask);

        latch.await(); // 두 스레드 완료 대기
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS); // 로그 flush 시간 확보
        System.out.flush(); // 강제 flush
    }


}
