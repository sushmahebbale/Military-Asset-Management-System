package com.militaryams.backend.controller;

import com.militaryams.backend.entity.Asset;
import com.militaryams.backend.repository.AssetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetRepository assetRepository;

    public AssetController(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        return assetRepository.save(asset);
    }
    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
    Asset existingAsset = assetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asset not found"));

    existingAsset.setAssetName(asset.getAssetName());
    existingAsset.setAssetType(asset.getAssetType());
    existingAsset.setStatus(asset.getStatus());
    existingAsset.setLocation(asset.getLocation());

    return assetRepository.save(existingAsset);
    }
}
