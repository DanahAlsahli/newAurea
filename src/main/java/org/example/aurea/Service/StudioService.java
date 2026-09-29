package org.example.aurea.Service;

import org.example.aurea.Api.ApiException;
import org.example.aurea.Model.Studio;
import org.example.aurea.Repository.StudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService {

    private final StudioRepository studioRepository;

    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public Studio addStudio(Studio studio) {

        return studioRepository.save(studio);
    }

    public List<Studio> getAllStudios() {

        return studioRepository.findAll();
    }

    public Studio getStudioById(Integer id) {

        return studioRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Studio not found"));
    }

    public void updateStudio(
            Integer id,
            Studio studio) {

        Studio existingStudio =
                studioRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Studio not found"));

        existingStudio.setProjectId(
                studio.getProjectId());

        existingStudio.setType(
                studio.getType());

        existingStudio.setStatus(
                studio.getStatus());

        studioRepository.save(existingStudio);
    }

    public void deleteStudio(Integer id) {

        Studio existingStudio =
                studioRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Studio not found"));

        studioRepository.delete(existingStudio);
    }
}


