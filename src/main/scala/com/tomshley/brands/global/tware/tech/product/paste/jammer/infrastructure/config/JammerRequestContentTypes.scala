package com.tomshley.brands.global.tware.tech.product.paste.jammer.infrastructure.config

import akka.http.scaladsl.model.*

/*
 *  |   Extension	| Kind of document                                  |	MIME Type
 *  |   .aac      | AAC audio	                                        |	audio/aac
 *  |   .abw      | AbiWord document	                                |	application/x-abiword
 *  |   .arc      | Archive document (multiple files embedded)         |	application/x-freearc
 *  |   .avif     | AVIF image	                                      |	image/avif
 *  |   .avi      | AVI: Audio Video Interleave	                      |	video/x-msvideo
 *  |   .azw      | Amazon Kindle eBook format	                      |	application/vnd.amazon.ebook
 *  |   .bin      | Any kind of binary data	                          |	application/octet-stream
 *  |   .bmp      | Windows OS/2 Bitmap Graphics	                    |	image/bmp
 *  |   .bz       | BZip archive	                                    |	application/x-bzip
 *  |   .bz2      | BZip2 archive	                                    |	application/x-bzip2
 *  |   .cda      | CD audio	                                        |	application/x-cdf
 *  |   .csh      | C-Shell script	                                  |	application/x-csh
 *  |   .css      | Cascading Style Sheets (CSS)	                    |	text/css
 *  |   .csv      | Comma-separated values (CSV)	                    |	text/csv
 *  |   .doc      | Microsoft Word	                                  |	application/msword
 *  |   .docx     | Microsoft Word (OpenXML)	                        |	application/vnd.openxmlformats-officedocument.wordprocessingml.document
 *  |   .eot      | MS Embedded OpenType fonts	                      |	application/vnd.ms-fontobject
 *  |   .epub     | Electronic publication (EPUB)	a                   |	pplication/epub+zip
 *  |   .gz       | GZip Compressed Archive	                          |	application/gzip
 *  |   .gif      | Graphics Interchange Format (GIF)	                |	image/gif
 *  |   .htm      | .html	HyperText Markup Language (HTML)	          |	text/html
 *  |   .ico      | Icon format	                                      |	image/vnd.microsoft.icon
 *  |   .ics      | iCalendar format	                                |	text/calendar
 *  |   .jar      | Java Archive (JAR)	                              |	application/java-archive
 *  |   .jpeg     | .jpg	JPEG images	                                |	image/jpeg
 *  |   .js       | JavaScript	                                      |	text/javascript (Specifications: HTML and RFC 9239)
 *  |   .json     | JSON format	                                      |	application/json
 *  |   .jsonld   | JSON-LD format	                                  |	application/ld+json
 *  |   .mid      | .midi	Musical Instrument Digital Interface (MIDI) |	audio/midi, audio/x-midi
 *  |   .mjs      | JavaScript module	                                |	text/javascript
 *  |   .mp3      | MP3 audio	                                        |	audio/mpeg
 *  |   .mp4      | MP4 video	                                        |	video/mp4
 *  |   .mpeg     | MPEG Video	                                      |	video/mpeg
 *  |   .mpkg     | Apple Installer Package	                          |	application/vnd.apple.installer+xml
 *  |   .odp      | OpenDocument presentation document	              |	application/vnd.oasis.opendocument.presentation
 *  |   .ods      | OpenDocument spreadsheet document	                |	application/vnd.oasis.opendocument.spreadsheet
 *  |   .odt      | OpenDocument text document	                      |	application/vnd.oasis.opendocument.text
 *  |   .oga      | OGG audio	                                        |	audio/ogg
 *  |   .ogv      | OGG video	                                        |	video/ogg
 *  |   .ogx      | OGG	                                              |	application/ogg
 *  |   .opus     | Opus audio	                                      |	audio/opus
 *  |   .otf      | OpenType font	                                    |	font/otf
 *  |   .png      | Portable Network Graphics	                        |	image/png
 *  |   .pdf      | Adobe Portable Document Format (PDF)	            |	application/pdf
 *  |   .php      | Hypertext Preprocessor (Personal Home Page)	      |	application/x-httpd-php
 *  |   .ppt      | Microsoft PowerPoint	                            |	application/vnd.ms-powerpoint
 *  |   .pptx     | Microsoft PowerPoint (OpenXML)	                  |	application/vnd.openxmlformats-officedocument.presentationml.presentation
 *  |   .rar      | RAR archive	                                      |	application/vnd.rar
 *  |   .rtf      | Rich Text Format (RTF)	                          |	application/rtf
 *  |   .sh       | Bourne shell script	                              |	application/x-sh
 *  |   .svg      | Scalable Vector Graphics (SVG)	                  |	image/svg+xml
 *  |   .tar      | Tape Archive (TAR)	                              |	application/x-tar
 *  |   .tif      | .tiff	Tagged Image File Format (TIFF)	            |	image/tiff
 *  |   .ts       | MPEG transport stream	                            |	video/mp2t
 *  |   .ttf      | TrueType Font	                                    |	font/ttf
 *  |   .txt      | Text, (generally ASCII or ISO 8859-n)	            |	text/plain
 *  |   .vsd      | Microsoft Visio	                                  |	application/vnd.visio
 *  |   .wav      | Waveform Audio Format	                            |	audio/wav
 *  |   .weba     | WEBM audio	                                      |	audio/webm
 *  |   .webm     | WEBM video	                                      |	video/webm
 *  |   .webp     | WEBP image	                                      |	image/webp
 *  |   .woff     | Web Open Font Format (WOFF)	                      |	font/woff
 *  |   .woff2    | Web Open Font Format (WOFF)	                      |	font/woff2
 *  |   .xhtml    | XHTML	                                            |	application/xhtml+xml
 *  |   .xls      | Microsoft Excel	                                  |	application/vnd.ms-excel
 *  |   .xlsx     | Microsoft Excel (OpenXML)	                        |	application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
 *  |   .xml      | XML	                                              |	application/xml is recommended as of RFC 7303 (section 4.1), but text/xml is still used sometimes. You can assign a specific MIME type to a file with .xml extension depending on how its contents are meant to be interpreted. For instance, an Atom feed is application/atom+xml, but application/xml serves as a valid default.
 *  |   .xul      | XUL	                                              |	application/vnd.mozilla.xul+xml
 *  |   .zip      | ZIP archive	                                      |	application/zip
 *  |   .3gp      | 3GPP audio/video container	                      |	video/3gpp; audio/3gpp if it doesn't contain video
 *  |   .3g2      | 3GPP2 audio/video container	                      |	video/3gpp2; audio/3gpp2 if it doesn't contain video
 *  |   .7z       | 7-zip archive	                                    |	application/x-7z-compressed
 */
enum JammerRequestContentTypes(fileExtension: String, mimeTypeValue: String, resourceDirectoryName: String) {
  case JS extends JammerRequestContentTypes("js", "text/javascript", "scripts")
  case CSS extends JammerRequestContentTypes("css", "text/css", "styles")

  def toExtension: String = fileExtension

  def toDirectoryName: String = resourceDirectoryName

  def toContentType: ContentType.WithCharset = ContentType(
    this.toMediaType,
    HttpCharsets.`UTF-8`
  )

  def toMediaType: MediaType.WithOpenCharset = {
    var mimeSub = this.toMime.split("/").last
    MediaType.customWithOpenCharset(
      this.toMime.split(s"/${mimeSub}").head,
      mimeSub,
      fileExtensions = List("js")
    )
  }

  def toMime: String = mimeTypeValue
}

