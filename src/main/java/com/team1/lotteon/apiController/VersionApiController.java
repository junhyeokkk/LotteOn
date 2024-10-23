package com.team1.lotteon.apiController;

import com.team1.lotteon.dto.VersionDTO;
import com.team1.lotteon.entity.Version;
import com.team1.lotteon.service.admin.VersionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


    @PostMapping("/api/version")
    public ResponseEntity<VersionDTO> createVersion(@RequestBody VersionDTO versionDTO) {
        Version savedVersion = versionService.insertVersion(versionDTO);
        return ResponseEntity.ok(versionDTO);
    }

}
