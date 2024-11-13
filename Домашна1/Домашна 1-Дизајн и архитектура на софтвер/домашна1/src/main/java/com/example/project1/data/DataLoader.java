package com.example.project1.data;

import com.example.project1.data.pipeline.Pipe;
import com.example.project1.data.pipeline.impl.FilterOne;
import com.example.project1.data.pipeline.impl.FilterTwo;
import com.example.project1.data.pipeline.impl.FilterThree;
import com.example.project1.entity.CompanyProfile;
import com.example.project1.repository.CompanyProfileRepository;
import com.example.project1.repository.AssetHistoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final CompanyProfileRepository companyProfileRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    @PostConstruct
    private void initializeData() throws IOException, ParseException {
        long startTime = System.nanoTime();
        Pipe<List<CompanyProfile>> pipe = new Pipe<>();
        pipe.addFilter(new FilterOne(companyProfileRepository));
        pipe.addFilter(new FilterTwo(companyProfileRepository, assetHistoryRepository));
        pipe.addFilter(new FilterThree(companyProfileRepository, assetHistoryRepository));
        pipe.runFilter(null);
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;

        long hours = durationInMillis / 3_600_000;
        long minutes = (durationInMillis % 3_600_000) / 60_000;
        long seconds = (durationInMillis % 60_000) / 1_000;

        System.out.printf("Total time for all filters to complete: %02d hours, %02d minutes, %02d seconds%n",
                hours, minutes, seconds);

    }

}
