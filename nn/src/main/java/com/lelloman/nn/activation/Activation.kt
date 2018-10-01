package com.lelloman.nn.activation

enum class Activation(val factory: (Int, Int) -> LayerActivation) {
    LOGISTIC(::LogisticActivation),
    TANH(::TanhActivation),
    RELU(::ReluActivation),
    LEAKY_RELU(::LeakyReluActivation),
    SOFTMAX(::SoftmaxActivation),
    IDENTITY(::IdentityActivation)
}