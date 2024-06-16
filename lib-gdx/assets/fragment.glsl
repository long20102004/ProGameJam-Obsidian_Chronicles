#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_playerPos;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords) * v_color;
    float distance = length(v_texCoords - u_playerPos);
    float lightness = clamp(1.0 - distance, 0.0, 1.0);
    gl_FragColor = mix(color, vec4(1.0, 1.0, 1.0, color.a), lightness);
}