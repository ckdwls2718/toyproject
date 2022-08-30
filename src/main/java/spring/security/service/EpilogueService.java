package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.RequestEpilogueDto;
import spring.security.dto.ResponseEpilogueDto;
import spring.security.entity.Board;
import spring.security.entity.Epilogue;
import spring.security.entity.EpilogueImage;
import spring.security.repository.EpilogueImageRepository;
import spring.security.repository.EpilogueRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpilogueService {

    private final EpilogueRepository epilogueRepository;
    private final EpilogueImageRepository epilogueImageRepository;

    @Transactional
    public void save(RequestEpilogueDto epilogueDto, List<String> urlList) {

        Epilogue epilogue = epilogueRepository.save(
                Epilogue.builder()
                        .content(epilogueDto.getContent())
                        .board(Board.builder()
                                .id(epilogueDto.getBoardId())
                                .build())
                        .build());

        for (String url : urlList) {
            epilogueImageRepository.save(EpilogueImage.builder()
                    .url(url)
                    .epilogue(epilogue)
                    .build());

        }
    }

    public ResponseEpilogueDto findOne(Long boardId) {
        Epilogue findEpilogue = epilogueRepository.findByBoardId(boardId);

        return ResponseEpilogueDto.builder()
                .id(findEpilogue.getId())
                .content(findEpilogue.getContent())
                .build();
    }
}
