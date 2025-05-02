import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import entidades.Figura;
import entidades.NotaMusical;

public class ReproductorAudioMIDI {

    private static final int VELOCIDAD = 100; // Volumen de la nota
    private static final int CANAL_PIANO = 0; // Canal MIDI para piano

    // Mapeo de notas a valores MIDI
    private static final int[] NOTAS_MIDI = {
        60, // DO
        61, // DO#
        62, // RE
        63, // RE#
        64, // MI
        65, // FA
        66, // FA#
        67, // SOL
        68, // SOL#
        69, // LA
        70, // LA#
        71  // SI
    };
    

    // Duraciones en milisegundos para cada figura musical
    // Duraciones en milisegundos para cada figura musical
    private static final int DURACION_REDONDA = 1600;
    private static final int DURACION_BLANCA = 800;
    private static final int DURACION_NEGRA = 400;
    private static final int DURACION_CORCHEA = 200;
    private static final int DURACION_SEMICORCHEA = 100;
    private static final int DURACION_FUSA = 50;
    private static final int DURACION_SEMIFUSA = 25;


    // Reproduce una única nota
    public static void reproducirNota(NotaMusical notaMusical) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel piano = synth.getChannels()[CANAL_PIANO];
    
            int notaMidi = NOTAS_MIDI[notaMusical.getNota().ordinal()] + (notaMusical.getOctava() - 4) * 12;
    
            // Validar si la nota está dentro del rango MIDI audible
            if (notaMidi < 21 || notaMidi > 108) {
                System.out.println("Nota fuera de rango audible: " + notaMidi + " (Octava " + notaMusical.getOctava() + ")");
                synth.close();
                return;
            }
    
            int duracion = obtenerDuracion(notaMusical.getFigura());
    
            // Forzar duración mínima efectiva
            if (duracion < 100) duracion = 100;
    
            piano.noteOn(notaMidi, VELOCIDAD);
            Thread.sleep(duracion);
            piano.noteOff(notaMidi);
    
            synth.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    // Devuelve la duración de la nota según su figura
    private static int obtenerDuracion(Figura figura) {
        switch (figura) {
            case REDONDA:
                return DURACION_REDONDA;
            case BLANCA:
                return DURACION_BLANCA;
            case NEGRA:
                return DURACION_NEGRA;
            case CORCHEA:
                return DURACION_CORCHEA;
            case SEMICORCHEA:
                return DURACION_SEMICORCHEA;
            case FUSA:
                return DURACION_FUSA;
            case SEMIFUSA:
                return DURACION_SEMIFUSA;
            default:
                return DURACION_NEGRA;
        }
    }
    

}
