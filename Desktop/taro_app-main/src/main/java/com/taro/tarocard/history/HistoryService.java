package com.taro.tarocard.history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public void save(History history) {
        historyRepository.save(history);
    }

    public List<History> findByUserId(Long userId) {
        return historyRepository.findByUserId(userId);
    }
}
