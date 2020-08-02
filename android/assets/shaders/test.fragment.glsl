#ifdef GL_ES
precision mediump float;
#endif

varying vec2  v_texCoords;
varying float v_lightIntensity;

//uniform sampler2D u_texture;
uniform vec3 u_color;

void main() {
    //vec4 texCol  = texture( u_texture, v_texCoords.st );
    gl_FragColor = vec4(u_color * v_lightIntensity, 1.0);
    //gl_FragColor = vec4( texCol.rgb * v_lightIntensity, 1.0 );
}