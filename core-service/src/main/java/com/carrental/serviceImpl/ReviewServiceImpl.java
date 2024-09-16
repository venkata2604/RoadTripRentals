package com.carrental.serviceImpl;

import com.carrental.dto.ReviewDto;
import com.carrental.entity.ReviewEntity;
import com.carrental.entity.UserEntity;
import com.carrental.exception.ReviewNotFoundException;
import com.carrental.exception.UserNotFoundException;
import com.carrental.repository.ReviewRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ReviewDto> getReviews() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getReviewsByCar(Long carId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByCarId(carId);
        return reviews.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReview(Long id) throws ReviewNotFoundException {
        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty()) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            return mapToDto(reviewEntity.get());
        }
    }

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        ReviewEntity reviewEntity = modelMapper.map(reviewDto, ReviewEntity.class);
        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);
        return mapToDto(savedReviewEntity);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto updatedReviewDto) throws ReviewNotFoundException {
        Optional<ReviewEntity> existingReviewEntity = reviewRepository.findById(id);
        if (existingReviewEntity.isEmpty()) throw new ReviewNotFoundException("Review not found");

        ReviewEntity updatedReviewEntity = modelMapper.map(updatedReviewDto, ReviewEntity.class);
        updatedReviewEntity.setReviewId(id);
        reviewRepository.save(updatedReviewEntity);

        return mapToDto(updatedReviewEntity);
    }

    @Override
    public void deleteReview(Long id) throws ReviewNotFoundException {
        Optional<ReviewEntity> existingReviewEntity = reviewRepository.findById(id);
        if (existingReviewEntity.isEmpty()) throw new ReviewNotFoundException("Review not found");

        reviewRepository.deleteById(id);
    }

    private ReviewDto mapToDto(ReviewEntity reviewEntity){
        ReviewDto reviewDto = modelMapper.map(reviewEntity, ReviewDto.class);
        Optional<UserEntity> userEntity = userRepository.findById(reviewEntity.getUserId());
        userEntity.ifPresent(entity -> reviewDto.setUsername(entity.getFirstName() + " " + entity.getLastName()));
        return reviewDto;
    }


}
