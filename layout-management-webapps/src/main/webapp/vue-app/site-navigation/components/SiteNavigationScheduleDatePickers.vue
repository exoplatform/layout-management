<template>
  <div>
    <div class="d-flex flex-row">
      <v-icon
        size="24"
        class="pl-4">
        far fa-clock
      </v-icon>
      <date-picker
        v-model="startPublicationDate"
        :min-value="minimumStartDate"
        class="scheduleStartDatePicker pa-0 pl-4" />
      <time-picker
        v-model="startPublicationDateTime"
        :min="minimumStartTime"
        class="mb-3" />
    </div>
    <div class="d-flex flex-row pl-8">
      <date-picker
        v-model="endPublicationDate"
        :min-value="minimumEndDate"
        class="scheduleEndDatePicker pa-0 pl-6" />
      <time-picker
        v-model="endPublicationDateTime"
        :min="minimumEndTime"
        class="mb-3" />
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    startPublicationDate: new Date(),
    startPublicationDateTime: new Date(),
    endPublicationDate: new Date(),
    endPublicationDateTime: new Date(new Date().getTime() + 900000),
  }),
  computed: {
    minimumStartDate() {
      return new Date();
    },
    minimumStartTime() {
      return new Date(this.startPublicationDateTime.getTime() - 900000);
    },
    minimumEndDate() {
      if (!this.startPublicationDate) {
        return null;
      }
      return new Date(this.startPublicationDate);
    },
    minimumEndTime() {
      if (!this.startPublicationDate || !this.endPublicationDate || !this.startPublicationDateTime || !this.endPublicationDateTime) {
        return null;
      }
      this.$emit('change', this.startPublicationDate, this.endPublicationDate, this.startPublicationDateTime.getTime(), this.endPublicationDateTime.getTime());
      if (this.checkDatesOnSameDay(this.startPublicationDate, this.endPublicationDate)){ 
        return new Date(this.startPublicationDateTime.getTime() + 900000);
      }
      return null ;
    },
  },
  watch: {
    startPublicationDate(newVal, oldVal) {
      if (!newVal || !oldVal || new Date(newVal).getTime() === new Date(oldVal).getTime()) {
        return;
      }
      const newDate = new Date(newVal);
      this.endPublicationDate = new Date(newDate.getTime());
    },
  },
  methods: {
    checkDatesOnSameDay(firstDate, secondDate) {
      return new Date(firstDate).getFullYear() === new Date(secondDate).getFullYear() &&
            new Date(firstDate).getMonth() === new Date(secondDate).getMonth() &&
            new Date(firstDate).getDate() === new Date(secondDate).getDate();
    },
  }
};
</script>