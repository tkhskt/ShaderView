#version 300 es

precision mediump float;

uniform sampler2D uTexture;
uniform vec2 uPointer;
uniform float uVelo;

in vec2 textureCoord;
out vec4 fragColor;

uniform vec4 resolution;

float circle(vec2 uv, vec2 disc_center, float disc_radius, float border_size) {
    uv -= disc_center;
    float dist = sqrt(dot(uv, uv));
    return smoothstep(disc_radius+border_size, disc_radius-border_size, dist);
}

void main()    {
    vec2 newUV = (textureCoord - vec2(0.5))*resolution.zw + vec2(0.5);
    vec4 color = vec4(1., 0., 0., 1.);

    float c = circle(newUV, uPointer, 0.0, 0.8);
    float r = texture(uTexture, newUV.xy += c * (uVelo * .5)).x;
    float g = texture(uTexture, newUV.xy += c * (uVelo * .525)).y;
    float b = texture(uTexture, newUV.xy += c * (uVelo * .55)).z;
    color = vec4(r, g, b, 1.);

    fragColor = color;
}
