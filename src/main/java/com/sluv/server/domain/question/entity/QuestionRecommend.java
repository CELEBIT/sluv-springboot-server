package com.sluv.server.domain.question.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Recommend")
public class QuestionRecommend extends Question{

}
