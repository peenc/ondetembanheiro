package com.pedropc.ondetembanheiro.modeldto;

import com.pedropc.ondetembanheiro.model.Rating;

import java.util.List;

public record RatingSummaryDto(double average, List<Rating> list) {}