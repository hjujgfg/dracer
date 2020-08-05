#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying vec3 v_vertPosWorld;
varying vec3 v_vertNVWorld;

uniform vec3 lightColor;
uniform vec3 lightPosition;
uniform vec3 u_color;

void main() {
    vec3  toLightVector  = normalize(lightPosition - v_vertPosWorld.xyz);
    float lightIntensity = max(0.0, dot(v_vertNVWorld, toLightVector));
    vec3  finalCol       = u_color * lightIntensity * lightColor;
    gl_FragColor         = vec4( finalCol.rgb * lightIntensity, 1.0 );
    //gl_FragColor = vec4( texCol.rgb * v_lightIntensity, 1.0 );
}