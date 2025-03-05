package com.david.writing.persistence;

import java.util.List;
import com.david.writing.domain.ApiUsage;

public interface ApiUsageStorage {
    void saveApiUsage(List<ApiUsage> usageHistory);
    List<ApiUsage> loadApiUsage();
} 