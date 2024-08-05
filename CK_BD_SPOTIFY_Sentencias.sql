
//Media
SELECT a.id_media, a.nombre_media, d.tipo_media, nvl(e.nombre_idioma, 'Ninguno') nombre_idioma, nvl(COALESCE(f.nombre_genero_musical, h.genero_podcast), 'Sin genero') AS genero, count(1) as union_c FROM tbl_media a LEFT JOIN tbl_canciones b ON a.id_media = b.id_cancion LEFT JOIN tbl_podcasts c ON a.id_media = c.id_podcast INNER JOIN tbl_tipo_media d ON a.id_tipo_media = d.id_tipo_media LEFT JOIN tbl_idiomas e ON b.id_idioma = e.id_idioma OR c.id_idioma = e.id_idioma LEFT JOIN tbl_generos_musicales f ON b.id_genero_musical = f.id_genero_musical LEFT JOIN tbl_podcast_x_generos g ON c.id_podcast = g.id_podcast LEFT JOIN tbl_genero_podcast h ON c.id_podcast = h.id_genero_podcast group by a.id_media, a.nombre_media, d.tipo_media, nvl(e.nombre_idioma, 'Ninguno') , nvl(COALESCE(f.nombre_genero_musical, h.genero_podcast), 'Sin genero');
       
//Tiempo     
select row_number() over (order by fecha_reproduccion) as id_tiempo ,to_char(a.fecha_reproduccion, 'yyyy') anio, to_char(a.fecha_reproduccion, 'month') mes,  to_char(a.fecha_reproduccion, 'day') dia  from tbl_historial_media a;

//artistas
select * from tbl_usuarios where id_tipo_usuario in (1,3);

//Usuarios
select a.id_usuario, b.id_historial_de_reproduccion, a.nombre_usuario,nvl( d.nombre_plan, 'Sin plan') nombre_plan, e.nombre_genero from tbl_usuarios a inner join tbl_usuario_estandar b on a.id_usuario = b.id_usuario left join tbl_pago_planes c on a.id_usuario = c.id_usuario left join tbl_planes d on c.id_plan = d.id_plan left join tbl_genero e on a.id_genero = e.id_genero where id_tipo_usuario = 2;

//Hechos
SELECT ROW_NUMBER() OVER (ORDER BY a.id_media) AS id_hechos, NVL(NVL(b.id_artista, c.id_podcaster), 0) AS id_artistas, NVL(f.id_pais, 0) AS id_pais, a.id_media, NVL(d.id_historial_reproduccion, 0) AS id_tiempo_reproduccion, NVL(e.id_usuario, 0) AS id_usuario, COUNT(d.id_historial_reproduccion) AS cantidad_reproducciones, COUNT(NVL(b.id_artista, c.id_podcaster)) AS cantidad_contenido  FROM tbl_media a LEFT JOIN tbl_canciones b ON a.id_media = b.id_cancion LEFT JOIN tbl_podcasts c ON a.id_media = c.id_podcast LEFT JOIN tbl_historial_media d ON a.id_media = d.id_media LEFT JOIN tbl_usuario_estandar e ON d.id_historial_reproduccion = e.id_historial_de_reproduccion inner JOIN tbl_usuarios f ON e.id_usuario = f.id_usuario GROUP BY a.id_media, NVL(NVL(b.id_artista, c.id_podcaster), 0), NVL(f.id_pais, 0), NVL(d.id_historial_reproduccion, 0), NVL(e.id_usuario, 0) HAVING NVL(NVL(b.id_artista, c.id_podcaster), 0) != 0 OR NVL(f.id_pais, 0) != 0 OR a.id_media IS NOT NULL OR NVL(d.id_historial_reproduccion, 0) != 0 OR NVL(e.id_usuario, 0) != 0;

SELECT * from tbl_paises;


