package com.team1.lotteon.apiController.admin.config;

import com.team1.lotteon.dto.RequestDTO.VersionDeleteRequestDTO;
import com.team1.lotteon.dto.VersionDTO;
import com.team1.lotteon.entity.Version;
import com.team1.lotteon.service.admin.VersionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class VersionApiController {

    private final VersionService versionService;

    private final ModelMapper modelMapper;

    @PostMapping("/api/version")
    public ResponseEntity<VersionDTO> createVersion(@RequestBody VersionDTO versionDTO) {

        Version savedVersion = versionService.insertVersion(versionDTO);

        VersionDTO saveVersionDTO = modelMapper.map(savedVersion, VersionDTO.class);

        return ResponseEntity.ok(saveVersionDTO);
    }

    @PostMapping("/api/version/delete")
    public ResponseEntity<String> deleteVersions(@RequestBody VersionDeleteRequestDTO request) {
        versionService.deleteVersionsByIds(request.getIds());
        return ResponseEntity.ok("선택된 버전이 삭제되었습니다.");
    }

}
