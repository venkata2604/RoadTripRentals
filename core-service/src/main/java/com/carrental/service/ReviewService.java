package com.carrental.service;

import com.carrental.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewDto> getReviews();

    ReviewDto getReview(Long id);

    ReviewDto addReview(ReviewDto reviewDto);

    ReviewDto updateReview(Long id, ReviewDto updatedReviewDto);

    void deleteReview(Long id);

    List<ReviewDto> getReviewsByCar(Long carId);
}
