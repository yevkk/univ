using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDB_CW.Models
{
    public class CheckIn
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("ts")]
        [Display(Name = "Timestamp")]
        public DateTime Ts { get; set; }

        [ForeignKey("Booking")]
        [Column("booking_id")]
        [Display(Name = "Booking")]
        public int BookingId { get; set; }

        public Booking? Booking { get; set; }
    }
}
