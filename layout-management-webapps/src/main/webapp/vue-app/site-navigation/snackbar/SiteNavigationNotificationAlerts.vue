<!--
Copyright (C) 2023 eXo Platform SAS.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->
<template>
  <v-snackbar
    :value="displayAlerts"
    color="transparent"
    elevation="0"
    app
    bottom
    left>
    <site-navigation-notification-alert
      v-for="alert in alerts"
      :key="alert.message"
      :alert="alert"
      :is-mobile="isMobile"
      @dismissed="deleteAlert(alert)" />
  </v-snackbar>
</template>

<script>

export default {
  data: () => ({
    alerts: [],
  }),
  computed: {
    displayAlerts() {
      return this.alerts && this.alerts.length;
    },
  },
  created() {
    this.$root.$on('navigation-node-notification-alert', alert => this.alerts.push(alert));
    this.$root.$on('confirm-node-deletion', (node) => {
      if (node && node.id) {
        const clickMessage = this.$t('siteNavigation.label.undoDelete');
        const message = this.$t('siteNavigation.label.deleteSuccess');
        const administratorMessage = this.$t('siteNavigation.label.contact.administrator');
        this.$root.$emit('navigation-node-notification-alert', {
          message,
          administratorMessage,
          type: 'success',
          click: () => this.undoDeleteNode(node.id),
          clickMessage,
        });
      }
    });
  },
  methods: {
    addAlert(alert) {
      const time = 5000;
      if (alert) {
        this.alerts.push(alert);
        window.setTimeout(() => this.deleteAlert(alert), time);
      }
    },
    deleteAlert(alert) {
      const index = this.alerts.indexOf(alert);
      this.alerts.splice(index, 1);
      this.$forceUpdate();
    },
    undoDeleteNode(nodeId) {
      return this.$siteNavigationService.undoDeleteNode(nodeId)
        .then(() => {
          this.deleteAlert(alert);
          this.addAlert({
            message: this.$t('siteNavigation.deleteCanceled'),
            type: 'success',
          });
        });
    }
  },
};
</script>