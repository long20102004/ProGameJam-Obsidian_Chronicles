#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_lightTexture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords) * v_color;
    vec4 lightColor = texture2D(u_lightTexture, v_texCoords);
    gl_FragColor = color * lightColor;
}